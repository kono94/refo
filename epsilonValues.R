# Libraries
library(ggplot2)
library(matrixStats)
library(MASS)
library(reshape2)
library(reshape)
# file.choose()
convergence <- read.csv(file.choose(), header=FALSE, row.names=1, skip=1)

sds <- rowSds(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
men <- rowMeans(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
print(sds)

# create dummy data
data <- data.frame(
  names=rownames(convergence),
  means=men,
  sds=sds
)
print(length(men))

convergence$group <- row.names(convergence)
convergence.m <- melt(convergence, id.vars = "group")

ggplot(data, aes(x=men, y=sds)) +
  geom_boxplot()
ggplot(data) +
  geom_bar(aes(x=names, y=means, fill=means), stat="identity", color="black", alpha=0.8) +
  geom_errorbar( aes(x=names, ymin=means, ymax=means+sds), width=0.4, colour="black", alpha=0.8, size=0.6) +
  #geom_text(aes(label=as.integer(means), x =names, y=means), position=position_dodge(width=0.9), vjust=-0.25) +
  xlab("Epsilon") + ylab("avg. amount of episodes until convergence")


#ba <- barplot(names=rownames(convergence), height=men, ylim=c(0, max(men)*1.2), ylab = "avg. episodes until convergence", xlab = "epsilon value")
#text(x = ba, y = men, label = as.integer(men), pos = 3, cex = 0.8, col = "red")
